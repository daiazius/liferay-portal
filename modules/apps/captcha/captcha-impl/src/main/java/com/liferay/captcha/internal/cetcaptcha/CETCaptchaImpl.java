package com.liferay.captcha.internal.cetcaptcha;

import com.liferay.captcha.configuration.CETCaptchaConfiguration;
import com.liferay.captcha.configuration.CaptchaConfiguration;
import com.liferay.captcha.simplecaptcha.SimpleCaptchaImpl;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.captcha.Captcha;
import com.liferay.portal.kernel.captcha.CaptchaConfigurationException;
import com.liferay.portal.kernel.captcha.CaptchaException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;

@Component(
	configurationPid = "com.liferay.captcha.configuration.CETCaptchaConfiguration",
	factory = "com.liferay.captcha.internal.cetcaptcha.CETCaptchaImpl",
	service = Captcha.class
)
public class CETCaptchaImpl extends SimpleCaptchaImpl {

	@Activate
	protected void activate(Map<String, Object> properties) throws Exception{
		_cetCaptchaConfiguration = ConfigurableUtil.createConfigurable(
			CETCaptchaConfiguration.class, properties);

		setCaptchaConfiguration(_configurationProvider.getSystemConfiguration(
			CaptchaConfiguration.class));

	}

	@Override
	public String getTaglibPath() {
		return _TAGLIB_PATH;
	}


	@Override
	protected boolean validateChallenge(HttpServletRequest httpServletRequest)
	throws CaptchaException {

		String hCaptchaResponse = ParamUtil.getString(
			httpServletRequest, _cetCaptchaConfiguration.responseParameter());

		while (Validator.isBlank(hCaptchaResponse) &&
			   (httpServletRequest instanceof HttpServletRequestWrapper)) {

			HttpServletRequestWrapper httpServletRequestWrapper =
				(HttpServletRequestWrapper)httpServletRequest;

			httpServletRequest =
				(HttpServletRequest)httpServletRequestWrapper.getRequest();

			hCaptchaResponse = ParamUtil.getString(
				httpServletRequest, _cetCaptchaConfiguration.responseParameter());
		}

		if (Validator.isBlank(hCaptchaResponse)) {
			_log.error(
				"CAPTCHA text is null. User " +
				httpServletRequest.getRemoteUser() +
				" may be trying to circumvent the CAPTCHA.");

			throw new CaptchaException();
		}

		Http.Options options = new Http.Options();

		options.setLocation(_cetCaptchaConfiguration.verifyURL());

		try {
			options.addPart("secret", _cetCaptchaConfiguration.apiKey());
		}
		catch (SystemException systemException) {
			_log.error(systemException);
		}

		options.addPart("remoteip", httpServletRequest.getRemoteAddr());
		options.addPart("response", hCaptchaResponse);
		options.setPost(true);

		String content = null;

		try {
			content = HttpUtil.URLtoString(options);
		}
		catch (IOException ioException) {
			_log.error(ioException);

			throw new CaptchaConfigurationException();
		}

		if (content == null) {
			_log.error("hCaptcha did not return a result");

			throw new CaptchaConfigurationException();
		}

		try {
			JSONObject jsonObject = _jsonFactory.createJSONObject(content);

			String success = jsonObject.getString("success");

			if (StringUtil.equalsIgnoreCase(success, "true")) {
				return true;
			}

			JSONArray jsonArray = jsonObject.getJSONArray("error-codes");

			if ((jsonArray == null) || (jsonArray.length() == 0)) {
				_log.error("hCaptcha encountered an error");

				throw new CaptchaConfigurationException();
			}

			StringBundler
				sb = new StringBundler((jsonArray.length() * 2) - 1);

			for (int i = 0; i < jsonArray.length(); i++) {
				sb.append(jsonArray.getString(i));

				if (i < (jsonArray.length() - 1)) {
					sb.append(StringPool.COMMA_AND_SPACE);
				}
			}

			_log.error("hCaptcha encountered an error: " + sb.toString());

			throw new CaptchaConfigurationException();
		}
		catch (JSONException jsonException) {
			_log.error(
				"hCaptcha did not return a valid result: " + content,
				jsonException);

			throw new CaptchaConfigurationException();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(CETCaptchaImpl.class);

	private static final String _TAGLIB_PATH = "/captcha/cxcaptcha.jsp";

	private volatile CETCaptchaConfiguration _cetCaptchaConfiguration;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private JSONFactory _jsonFactory;
}

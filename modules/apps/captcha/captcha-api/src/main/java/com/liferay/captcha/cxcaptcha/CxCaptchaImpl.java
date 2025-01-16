package com.liferay.captcha.cxcaptcha;

import com.liferay.captcha.simplecaptcha.SimpleCaptchaImpl;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
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
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	configurationPid = "com.liferay.captcha.configuration.CaptchaConfiguration",
	property = "captcha.engine.impl=com.liferay.captcha.cxcaptcha.CxCaptchaImpl",
	service = Captcha.class
)
public class CxCaptchaImpl extends SimpleCaptchaImpl {

	@Override
	public String getTaglibPath() {
		return _TAGLIB_PATH;
	}

	@Override
	public void serveImage(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		throw new UnsupportedOperationException();
	}

	@Override
	public void serveImage(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean validateChallenge(HttpServletRequest httpServletRequest)
		throws CaptchaException {
		throw new CaptchaConfigurationException();
	}

	@Override
	protected boolean validateChallenge(PortletRequest portletRequest)
		throws CaptchaException {

		return validateChallenge(
			PortalUtil.getHttpServletRequest(portletRequest));
	}

	private static final String _TAGLIB_PATH = "/captcha/cxcaptcha.jsp";

	private static final Log _log = LogFactoryUtil.getLog(CxCaptchaImpl.class);

	@Reference
	private JSONFactory _jsonFactory;

}

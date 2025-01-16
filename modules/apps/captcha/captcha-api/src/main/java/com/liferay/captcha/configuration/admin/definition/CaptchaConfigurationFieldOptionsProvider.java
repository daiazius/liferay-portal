package com.liferay.captcha.configuration.admin.definition;

import com.liferay.captcha.provider.CaptchaProvider;
import com.liferay.configuration.admin.definition.ConfigurationFieldOptionsProvider;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.captcha.Captcha;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Locale;

@Component(
	property = {
		"configuration.field.name=captchaEngine",
		"configuration.pid=com.liferay.captcha.configuration.CaptchaConfiguration",
	},
	service = ConfigurationFieldOptionsProvider.class
)
public class CaptchaConfigurationFieldOptionsProvider implements ConfigurationFieldOptionsProvider {

	@Override
	public List<Option> getOptions() {
		return TransformUtil.transform(_captchaProvider.getCaptchas().entrySet(),
			captcha -> {

				return new Option() {
					@Override
					public String getLabel(Locale locale) {
						return captcha.getKey();
					}

					@Override
					public String getValue() {
						return captcha.getKey();
					}
				};
			});
	}

	@Reference
	private CaptchaProvider _captchaProvider;
}

package com.liferay.captcha.internal.cetcaptcha;

import com.liferay.osgi.util.configuration.ConfigurationFactoryUtil;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;

@Component(
	configurationPid = "com.liferay.captcha.configuration.CETCaptchaConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE, service = {}
)
public class CETCaptchaImplFactory {

	@Activate
	protected void activate(
		ComponentContext componentContext, Map<String, Object> properties)
		throws Exception {

		String externalReferenceCode =
			ConfigurationFactoryUtil.getExternalReferenceCode(properties);

		_componentInstance = _componentFactory.newInstance(
			HashMapDictionaryBuilder.<String, Object>put("captcha.engine.impl", externalReferenceCode).putAll(
				properties
			).remove(Constants.SERVICE_PID).build());
	}

	@Deactivate
	protected void deactivate() {
		if (_componentInstance != null) {
			_componentInstance.dispose();
		}
	}

	@Reference(
		target = "(component.factory=com.liferay.captcha.internal.cetcaptcha.CETCaptchaImpl)"
	)
	private ComponentFactory<CETCaptchaImpl> _componentFactory;

	private ComponentInstance<CETCaptchaImpl> _componentInstance;

}

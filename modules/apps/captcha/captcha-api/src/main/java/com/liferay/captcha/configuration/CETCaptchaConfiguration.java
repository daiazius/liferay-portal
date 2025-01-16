package com.liferay.captcha.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(generateUI = false)
@Meta.OCD(
	factory = true,
	id = "com.liferay.captcha.configuration.CETCaptchaConfiguration"
)
public interface CETCaptchaConfiguration {

	@Meta.AD
	public String apiKey();

	@Meta.AD
	public String resourcePath();

	@Meta.AD
	public String responseParameter();

	@Meta.AD
	public String verifyURL();

}

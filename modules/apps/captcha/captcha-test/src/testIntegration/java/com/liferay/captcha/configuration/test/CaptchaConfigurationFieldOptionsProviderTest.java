/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.captcha.configuration.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.captcha.simplecaptcha.SimpleCaptchaImpl;
import com.liferay.configuration.admin.definition.ConfigurationFieldOptionsProvider;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Objects;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

/**
 * @author Pedro Victor Silvestre
 */
@RunWith(Arquillian.class)
public class CaptchaConfigurationFieldOptionsProviderTest {

	@ClassRule
	@Rule
	public static final TestRule testRule = new LiferayIntegrationTestRule();

	@Test
	public void testGetOptionsWithCaptchaEngineField() {
		Assert.assertTrue(
			ListUtil.exists(
				_configurationFieldOptionsProvider.getOptions(),
				option -> Objects.equals(
					SimpleCaptchaImpl.class.getName(), option.getValue())));
	}

	@Inject(
		filter = "(&(configuration.pid=com.liferay.captcha.configuration.CaptchaConfiguration)(configuration.field.name=captchaEngine))"
	)
	private ConfigurationFieldOptionsProvider
		_configurationFieldOptionsProvider;

}
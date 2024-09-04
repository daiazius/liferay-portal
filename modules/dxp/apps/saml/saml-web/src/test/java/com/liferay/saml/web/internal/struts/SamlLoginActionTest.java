/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.saml.web.internal.struts;

import com.liferay.layout.seo.kernel.LayoutSEOLink;
import com.liferay.layout.seo.kernel.LayoutSEOLinkManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListMergeable;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.struts.Definition;
import com.liferay.portal.struts.TilesUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.saml.persistence.model.SamlSpIdpConnection;
import com.liferay.saml.persistence.service.SamlSpIdpConnectionLocalService;
import com.liferay.saml.runtime.configuration.SamlProviderConfigurationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Pedro Victor Silvestre
 */
public class SamlLoginActionTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_setUpPortal();
		_setUpSamlProviderConfigurationHelper();
		_setUpSamlSpIdpConnectionLocalService();
	}

	@Test
	public void testPageTitleIsSameWhenRedirectMessageIsDisabled()
		throws Exception {

		LayoutSEOLinkManager layoutSEOLinkManager = new LayoutSEOLinkManager() {

			@Override
			public LayoutSEOLink getCanonicalLayoutSEOLink(
					Layout layout, Locale locale, String canonicalURL,
					ThemeDisplay themeDisplay)
				throws PortalException {

				return null;
			}

			@Override
			public String getFullPageTitle(
				Layout layout, String portletId, String tilesTitle,
				ListMergeable<String> titleListMergeable,
				ListMergeable<String> subtitleListMergeable, String companyName,
				Locale locale) {

				return StringUtil.merge(
					new String[] {layout.getHTMLTitle(locale), companyName},
					_SEPARATOR);
			}

			@Override
			public List<LayoutSEOLink> getLocalizedLayoutSEOLinks(
					Layout layout, Locale locale, String canonicalURL,
					Set<Locale> availableLocales)
				throws PortalException {

				return null;
			}

		};

		ReflectionTestUtil.setFieldValue(
			_samlLoginAction, "_layoutSEOLinkManager", layoutSEOLinkManager);

		ReflectionTestUtil.setFieldValue(
			_samlLoginAction, "_jsonFactory", JSONFactoryUtil.getJSONFactory());

		Props props = Mockito.mock(Props.class);

		Mockito.when(
			props.get("saml.idp.redirect.message.enabled")
		).thenReturn(
			"false"
		);

		ReflectionTestUtil.setFieldValue(_samlLoginAction, "_props", props);

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		String htmlTitle = RandomTestUtil.randomString();

		Layout layout = Mockito.mock(Layout.class);

		Mockito.when(
			layout.getHTMLTitle(Mockito.any(Locale.class))
		).thenReturn(
			htmlTitle
		);

		mockHttpServletRequest.setAttribute(WebKeys.LAYOUT, layout);

		ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);

		Mockito.when(
			themeDisplay.getLocale()
		).thenReturn(
			LocaleUtil.ENGLISH
		);

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		_samlLoginAction.execute(
			mockHttpServletRequest, new MockHttpServletResponse());

		Definition definition = (Definition)mockHttpServletRequest.getAttribute(
			TilesUtil.DEFINITION);

		Map<String, String> definitionAttributes = definition.getAttributes();

		Assert.assertEquals(
			StringUtil.merge(
				new String[] {htmlTitle, _COMPANY_NAME}, _SEPARATOR),
			definitionAttributes.get("title"));
	}

	private void _setUpPortal() throws Exception {
		Company company = Mockito.mock(Company.class);

		Mockito.when(
			company.getName()
		).thenReturn(
			_COMPANY_NAME
		);

		Portal portal = Mockito.mock(Portal.class);

		Mockito.when(
			portal.getCompany(Mockito.any(HttpServletRequest.class))
		).thenReturn(
			company
		);

		Mockito.when(
			portal.getCompanyId(Mockito.any(HttpServletRequest.class))
		).thenReturn(
			RandomTestUtil.randomLong()
		);

		ReflectionTestUtil.setFieldValue(_samlLoginAction, "_portal", portal);
	}

	private void _setUpSamlProviderConfigurationHelper() {
		SamlProviderConfigurationHelper samlProviderConfigurationHelper =
			Mockito.mock(SamlProviderConfigurationHelper.class);

		Mockito.when(
			samlProviderConfigurationHelper.isEnabled()
		).thenReturn(
			true
		);

		Mockito.when(
			samlProviderConfigurationHelper.isRoleSp()
		).thenReturn(
			true
		);

		ReflectionTestUtil.setFieldValue(
			_samlLoginAction, "_samlProviderConfigurationHelper",
			samlProviderConfigurationHelper);
	}

	private void _setUpSamlSpIdpConnectionLocalService() {
		SamlSpIdpConnection samlSpIdpConnection = Mockito.mock(
			SamlSpIdpConnection.class);

		Mockito.when(
			samlSpIdpConnection.isEnabled()
		).thenReturn(
			true
		);

		List<SamlSpIdpConnection> samlSpIdpConnections = new ArrayList<>();

		samlSpIdpConnections.add(samlSpIdpConnection);

		SamlSpIdpConnectionLocalService samlSpIdpConnectionLocalService =
			Mockito.mock(SamlSpIdpConnectionLocalService.class);

		Mockito.when(
			samlSpIdpConnectionLocalService.getSamlSpIdpConnections(
				Mockito.anyLong())
		).thenReturn(
			samlSpIdpConnections
		);

		_listUtilMockedStatic.when(
			() -> ListUtil.filter(
				Mockito.anyList(), Mockito.any(Predicate.class))
		).thenReturn(
			samlSpIdpConnections
		);

		ReflectionTestUtil.setFieldValue(
			_samlLoginAction, "_samlSpIdpConnectionLocalService",
			samlSpIdpConnectionLocalService);
	}

	private static final String _COMPANY_NAME = RandomTestUtil.randomString();

	private static final String _SEPARATOR = " - ";

	private static final MockedStatic<ListUtil> _listUtilMockedStatic =
		Mockito.mockStatic(ListUtil.class);

	private final SamlLoginAction _samlLoginAction = new SamlLoginAction();

}
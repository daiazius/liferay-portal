/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.key.secret;

/**
 * @author Pedro Victor Silvestre
 */
public enum SecretNamespace {

	CONFIGURATION("config/"), PREFERENCE("preference/");

	public String getIdentifierPrefix() {
		return _identifierPrefix;
	}

	private SecretNamespace(String identifierPrefix) {
		_identifierPrefix = identifierPrefix;
	}

	private final String _identifierPrefix;

}
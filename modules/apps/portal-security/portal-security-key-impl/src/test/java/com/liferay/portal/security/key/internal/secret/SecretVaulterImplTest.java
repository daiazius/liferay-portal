/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.key.internal.secret;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.security.key.KeyReference;
import com.liferay.portal.security.key.KeyReferenceUtil;
import com.liferay.portal.security.key.secret.Secret;
import com.liferay.portal.security.key.secret.SecretManager;
import com.liferay.portal.security.key.secret.exception.SecretException;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Pedro Victor Silvestre
 */
public class SecretVaulterImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		ReflectionTestUtil.setFieldValue(
			_secretVaulterImpl, "_secretManager", _secretManager);
	}

	@Test
	public void testVault() throws Exception {
		long companyId = RandomTestUtil.randomLong();
		String identifier = RandomTestUtil.randomString();
		String value = RandomTestUtil.randomString();

		KeyReference keyReference = new KeyReference(
			identifier, RandomTestUtil.randomString(),
			KeyReference.Type.SECRET);

		AtomicReference<Secret> atomicReference = new AtomicReference<>();

		Mockito.when(
			_secretManager.putSecret(Mockito.eq(companyId), Mockito.any())
		).thenAnswer(
			invocationOnMock -> {
				Secret secret = invocationOnMock.getArgument(1);

				atomicReference.set(secret);

				Assert.assertEquals(value, new String(secret.getChars()));

				return keyReference;
			}
		);

		Assert.assertEquals(
			KeyReferenceUtil.toKeyReferenceString(keyReference),
			_secretVaulterImpl.vault(companyId, identifier, value));

		Secret secret = atomicReference.get();

		KeyReference secretKeyReference = secret.getKeyReference();

		Assert.assertEquals(identifier, secretKeyReference.getIdentifier());
		Assert.assertEquals(
			StringPool.STAR, secretKeyReference.getProviderId());

		Assert.assertTrue(secret.isDestroyed());
	}

	@Test
	public void testVaultWhenSecretManagerFails() throws Exception {
		SecretException secretException = new SecretException();

		Mockito.when(
			_secretManager.putSecret(Mockito.anyLong(), Mockito.any())
		).thenThrow(
			secretException
		);

		Assert.assertSame(
			secretException,
			Assert.assertThrows(
				SecretException.class,
				() -> _secretVaulterImpl.vault(
					RandomTestUtil.randomLong(), RandomTestUtil.randomString(),
					RandomTestUtil.randomString())));
	}

	@Mock
	private SecretManager _secretManager;

	private final SecretVaulterImpl _secretVaulterImpl =
		new SecretVaulterImpl();

}
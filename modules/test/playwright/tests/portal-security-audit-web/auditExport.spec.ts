/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {applicationsMenuPageTest} from '../../fixtures/applicationsMenuPageTest';
import {loginTest} from '../../fixtures/loginTest';

export const test = mergeTests(loginTest(), applicationsMenuPageTest);

test('LPD-40224: Check if the export audit events resource URL has date parameters', async ({
	applicationsMenuPage,
	page,
}) => {
	page.on('dialog', (dialog) => dialog.accept());

	await applicationsMenuPage.goToAudit();

	await page.locator('#toggle_id_audit_event_searchtoggleAdvanced').click();

	const startDateYear = await page.locator('#startDateYear');

	const startDateYearValue = await startDateYear.inputValue();

	await page.locator('.lexicon-icon-search').click();

	await page.waitForTimeout(500);

	const options = await page.getByLabel('Options');

	await options.click();

	const menuItem = await page.getByRole('menuitem', {
		name: 'Export Audit Events',
	});

	await menuItem.click();

	const downloadPromise = await page.waitForEvent('download');

	await expect(downloadPromise.url()).toContain(
		`startDateYear=${startDateYearValue}`
	);
});

/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {applicationsMenuPageTest} from '../../fixtures/applicationsMenuPageTest';
import {loginTest} from '../../fixtures/loginTest';

export const test = mergeTests(loginTest(), applicationsMenuPageTest);

const PRIVATE_RENDER_PARAMETER_NAMESPACE = 'priv_r_p_';

const dateFields = [
	'endDateAmPm',
	'endDateDay',
	'endDateHour',
	'endDateMinute',
	'endDateMonth',
	'endDateYear',
	'startDateAmPm',
	'startDateDay',
	'startDateHour',
	'startDateMinute',
	'startDateMonth',
	'startDateYear',
];

const fields = [
	'className',
	'classPK',
	'clientHost',
	'clientIP',
	'eventType',
	'serverName',
	'userName',
];

const fieldsWithoutNamespace = ['groupId', 'serverPort', 'userId'];

test('LPD-40224: Check if the export audit events resource URL has all search parameters', async ({
	applicationsMenuPage,
	page,
}) => {
	page.on('dialog', (dialog) => dialog.accept());

	await applicationsMenuPage.goToAudit();

	await page.locator('#toggle_id_audit_event_searchtoggleAdvanced').click();

	await page.locator('.lexicon-icon-search').click();

	await page.waitForTimeout(500);

	// Populate map with all the date parameters

	const dateValues = {};

	for (const field of dateFields) {
		const inputElement = page.locator(`#${field}`);
		const inputValue = await inputElement.inputValue();

		dateValues[field] = inputValue;
	}

	const options = page.getByLabel('Options');

	await options.click();

	const menuItem = page.getByRole('menuitem', {
		name: 'Export Audit Events',
	});

	await menuItem.click();

	const downloadPromise = await page.waitForEvent('download');

	const exportURL = downloadPromise.url();

	// Check if the URL has all the search parameters

	for (const field of dateFields) {
		expect(exportURL).toContain(`${field}=${dateValues[field]}`);
	}

	for (const field of fields) {
		expect(exportURL).toContain(PRIVATE_RENDER_PARAMETER_NAMESPACE + field);
	}

	for (const field of fieldsWithoutNamespace) {
		expect(exportURL).toContain(field);
	}
});

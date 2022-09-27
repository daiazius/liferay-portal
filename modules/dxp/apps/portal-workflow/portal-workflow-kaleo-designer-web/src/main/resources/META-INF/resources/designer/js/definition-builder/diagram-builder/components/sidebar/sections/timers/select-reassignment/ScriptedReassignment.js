/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import {ClaySelect} from '@clayui/form';
import ClayLayout from '@clayui/layout';
import ClayLink from '@clayui/link';
import React, {useContext, useEffect, useState} from 'react';

import {DiagramBuilderContext} from '../../../../../DiagramBuilderContext';
import SidebarPanel from '../../../SidebarPanel';

const scriptLanguageOptions = [
	{
		label: Liferay.Language.get('groovy'),
		value: 'groovy',
	},
	{
		label: Liferay.Language.get('java'),
		value: 'java',
	},
];

const ScriptedReassignment = ({actionData, setContentName}) => {
	const {selectedItem, setSelectedItem} = useContext(DiagramBuilderContext);

	const [showScriptData, setShowScriptData] = useState(
		selectedItem?.data.taskTimers?.reassignments?.script
	);

	const [scriptLanguage, setScriptLanguage] = useState(
		selectedItem?.data.taskTimers?.reassignments?.scriptLanguage || 'groovy'
	);

	const addSourceButtonName = Liferay.Language.get('add-source-code');

	const goToEditor = () => setContentName('scripted-reassignment');

	const deleteScript = () => {
		setSelectedItem((previous) => {
			return {
				...previous,
				data: {
					...previous.data,
					reassignments: null,
				},
			};
		});
	};

	useEffect(() => {
		setShowScriptData(actionData?.script);
	}, [actionData]);

	return (
		<SidebarPanel panelTitle={Liferay.Language.get('script')}>
			<label htmlFor="script-language">
				{Liferay.Language.get('script-language')}
			</label>

			<ClaySelect
				aria-label="Select"
				defaultValue={scriptLanguage}
				id="script-language"
				onChange={({target}) => {
					setScriptLanguage(target.value);
				}}
				onClickCapture={() => {
					setSelectedItem((previous) => ({
						...previous,
						data: {
							...previous.data,
							taskTimers: {
								...previous.data.taskTimers,
								reassignments: [
									{
										...previous.data.taskTimers
											.reassignments[0],
										scriptLanguage,
									},
								],
							},
						},
					}));
				}}
			>
				{scriptLanguageOptions &&
					scriptLanguageOptions.map((item) => (
						<ClaySelect.Option
							key={item.value}
							label={item.label}
							value={item.value}
						/>
					))}
			</ClaySelect>

			{showScriptData ? (
				<ClayLayout.ContentCol className="current-node-data-area" float>
					<ClayLayout.Row
						className="current-node-data-row"
						justify="between"
					>
						<ClayLink
							button={false}
							className="truncate-container"
							displayType="secondary"
							href="#"
							onClick={goToEditor}
						>
							<span>{Liferay.Language.get('script')}</span>
						</ClayLink>

						<ClayButtonWithIcon
							className="delete-button text-secondary trash-button"
							displayType="unstyled"
							onClick={deleteScript}
							symbol="trash"
						/>
					</ClayLayout.Row>
				</ClayLayout.ContentCol>
			) : (
				<ClayButton displayType="secondary" onClick={goToEditor}>
					{addSourceButtonName.toUpperCase()}
				</ClayButton>
			)}
		</SidebarPanel>
	);
};

export default ScriptedReassignment;

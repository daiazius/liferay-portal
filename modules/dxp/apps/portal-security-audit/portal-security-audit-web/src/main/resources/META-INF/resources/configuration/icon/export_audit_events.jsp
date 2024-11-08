<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<liferay-portlet:resourceURL id="/audit/export_audit_events" useNamespace="<%= false %>" var="exportURL">
	<portlet:param name="className" value="<%= String.valueOf(auditDisplayContext.getClassName()) %>" />
	<portlet:param name="classPK" value="<%= String.valueOf(auditDisplayContext.getClassPK()) %>" />
	<portlet:param name="clientHost" value="<%= String.valueOf(auditDisplayContext.getClientHost()) %>" />
	<portlet:param name="clientIP" value="<%= String.valueOf(auditDisplayContext.getClientIP()) %>" />
	<portlet:param name="endDateAmPm" value="<%= String.valueOf(auditDisplayContext.getEndDateAmPm()) %>" />
	<portlet:param name="endDateDay" value="<%= String.valueOf(auditDisplayContext.getEndDateDay()) %>" />
	<portlet:param name="endDateHour" value="<%= String.valueOf(auditDisplayContext.getEndDateHour()) %>" />
	<portlet:param name="endDateMinute" value="<%= String.valueOf(auditDisplayContext.getEndDateMinute()) %>" />
	<portlet:param name="endDateMonth" value="<%= String.valueOf(auditDisplayContext.getEndDateMonth()) %>" />
	<portlet:param name="endDateYear" value="<%= String.valueOf(auditDisplayContext.getEndDateYear()) %>" />
	<portlet:param name="eventType" value="<%= String.valueOf(auditDisplayContext.getEventType()) %>" />
	<portlet:param name="groupId" value="<%= String.valueOf(auditDisplayContext.getGroupId()) %>" />
	<portlet:param name="keywords" value="<%= String.valueOf(auditDisplayContext.getKeywords()) %>" />
	<portlet:param name="serverName" value="<%= String.valueOf(auditDisplayContext.getServerName()) %>" />
	<portlet:param name="serverPort" value="<%= String.valueOf(auditDisplayContext.getServerPort()) %>" />
	<portlet:param name="startDateAmPm" value="<%= String.valueOf(auditDisplayContext.getStartDateAmPm()) %>" />
	<portlet:param name="startDateDay" value="<%= String.valueOf(auditDisplayContext.getStartDateDay()) %>" />
	<portlet:param name="startDateHour" value="<%= String.valueOf(auditDisplayContext.getStartDateHour()) %>" />
	<portlet:param name="startDateMinute" value="<%= String.valueOf(auditDisplayContext.getStartDateMinute()) %>" />
	<portlet:param name="startDateMonth" value="<%= String.valueOf(auditDisplayContext.getStartDateMonth()) %>" />
	<portlet:param name="startDateYear" value="<%= String.valueOf(auditDisplayContext.getStartDateYear()) %>" />
	<portlet:param name="userId" value="<%= String.valueOf(auditDisplayContext.getUserId()) %>" />
	<portlet:param name="userName" value="<%= String.valueOf(auditDisplayContext.getUserName()) %>" />
</liferay-portlet:resourceURL>

<aui:script>
	Liferay.Util.setPortletConfigurationIconAction(
		'<portlet:namespace />exportAuditEvents',
		() => {
			Liferay.Util.openConfirmModal({
				message:
					'<liferay-ui:message key="warning-this-csv-file-contains-user-supplied-inputs" unicode="<%= true %>" />',
				onConfirm: (isConfirmed) => {
					if (isConfirmed) {
						submitForm(
							document.hrefFm,
							'<%= exportURL + "&compress=0&etag=0&strip=0" %>'
						);
					}
				},
			});
		}
	);
</aui:script>
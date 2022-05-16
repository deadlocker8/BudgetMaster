package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.utils.notification.NotificationLinkBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class NotificationLinkBuilderTest
{
	@Test
	void test_buildEditLink_noContextPath()
	{
		HttpServletRequest mockedRequest = mock(HttpServletRequest.class);

		Mockito.when(mockedRequest.getContextPath()).thenReturn("");

		assertThat(NotificationLinkBuilder.buildEditLink(mockedRequest, "My Link", "/accounts", 15))
				.isEqualTo("<a href=\"/accounts/15/edit\" class=\"text-default\">My Link</a>");
	}

	@Test
	void test_buildEditLink_withContextPath()
	{
		HttpServletRequest mockedRequest = mock(HttpServletRequest.class);

		Mockito.when(mockedRequest.getContextPath()).thenReturn("/contextMatters");

		assertThat(NotificationLinkBuilder.buildEditLink(mockedRequest, "My Link", "/accounts", 15))
				.isEqualTo("<a href=\"/contextMatters/accounts/15/edit\" class=\"text-default\">My Link</a>");
	}

	@Test
	void test_buildEditLink_largeID()
	{
		HttpServletRequest mockedRequest = mock(HttpServletRequest.class);

		Mockito.when(mockedRequest.getContextPath()).thenReturn("");

		assertThat(NotificationLinkBuilder.buildEditLink(mockedRequest, "My Link", "/accounts", 123456))
				.isEqualTo("<a href=\"/accounts/123456/edit\" class=\"text-default\">My Link</a>");
	}

	@Test
	void test_build_noContextPath()
	{
		HttpServletRequest mockedRequest = mock(HttpServletRequest.class);

		Mockito.when(mockedRequest.getContextPath()).thenReturn("");

		assertThat(NotificationLinkBuilder.build(mockedRequest, "My Link", "/accounts/0815"))
				.isEqualTo("<a href=\"/accounts/0815\">My Link</a>");
	}

	@Test
	void test_build_withContextPath()
	{
		HttpServletRequest mockedRequest = mock(HttpServletRequest.class);

		Mockito.when(mockedRequest.getContextPath()).thenReturn("/contextMatters");

		assertThat(NotificationLinkBuilder.build(mockedRequest, "My Link", "/accounts/0815"))
				.isEqualTo("<a href=\"/contextMatters/accounts/0815\">My Link</a>");
	}
}

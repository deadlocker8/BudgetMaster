package de.deadlocker8.budgetmaster.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "budgetmaster.database")
public class DatabaseConfigurationProperties
{
	private static final String PREFIX = "budgetmaster.database";

	@NotBlank
	private String type;

	@NotBlank
	private String hostname;

	@Min(1)
	@Max(65536)
	private Integer port;

	@NotBlank
	private String databaseName;

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getHostname()
	{
		return hostname;
	}

	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}

	public Integer getPort()
	{
		return port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}

	public String getDatabaseName()
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public List<String> getMissingAttributes()
	{
		final List<String> missingAttributes = new ArrayList<>();
		if(type == null)
		{
			missingAttributes.add(createMissingAttributeString("type"));
		}

		if(hostname == null)
		{
			missingAttributes.add(createMissingAttributeString("hostname"));
		}

		if(port == null)
		{
			missingAttributes.add(createMissingAttributeString("port"));
		}

		if(databaseName == null)
		{
			missingAttributes.add(createMissingAttributeString("databaseName"));
		}

		if(username == null)
		{
			missingAttributes.add(createMissingAttributeString("username"));
		}

		if(password == null)
		{
			missingAttributes.add(createMissingAttributeString("password"));
		}

		return missingAttributes;
	}

	private String createMissingAttributeString(String type)
	{
		return MessageFormat.format("{0}.{1}", PREFIX, type);
	}

	@Override
	public String toString()
	{
		return "DatabaseConfigurationProperties{" +
				"type='" + type + '\'' +
				", hostname='" + hostname + '\'' +
				", port=" + port +
				", databaseName='" + databaseName + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}

package de.deadlocker8.budgetmaster.databasemigrator.destination.settings;


import de.deadlocker8.budgetmaster.databasemigrator.CustomIdGenerator;
import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = TableNames.SETTINGS)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationSettings implements ProvidesID
{
	@Id
	@GeneratedValue(generator = "custom_generator")
	@GenericGenerator(name = "custom_generator", strategy = CustomIdGenerator.GENERATOR)
	private Integer ID;

	private String currency;

	@Column(name = "rest_activated")
	private boolean restActivated;

	private Integer language;

	@Column(name = "use_dark_theme")
	private boolean useDarkTheme;

	@Column(name = "show_categories_as_circles")
	private boolean showCategoriesAsCircles;

	@Column(name = "auto_update_check_enabled")
	private boolean autoUpdateCheckEnabled;

	@Column(name = "backup_reminder_activated")
	private boolean backupReminderActivated;

	@Column(name = "last_backup_reminder_date")
	private LocalDate lastBackupReminderDate;

	@Column(name = "search_items_per_page")
	private Integer searchItemsPerPage;

	@Column(name = "auto_backup_strategy")
	private Integer autoBackupStrategy;

	@Column(name = "auto_backup_days")
	private Integer autoBackupDays;

	@Column(name = "auto_backup_time")
	private Integer autoBackupTime;

	@Column(name = "auto_backup_files_to_keep")
	private Integer autoBackupFilesToKeep;

	@Column(name = "auto_backup_git_url")
	private String autoBackupGitUrl;

	@Column(name = "auto_backup_git_branch_name")
	private String autoBackupGitBranchName;

	@Column(name = "auto_backup_git_user_name")
	private String autoBackupGitUserName;

	@Column(name = "auto_backup_git_token")
	private String autoBackupGitToken;

	@Column(name = "installed_version_code")
	private Integer installedVersionCode;

	@Column(name = "whats_new_shown_for_current_version")
	private Boolean whatsNewShownForCurrentVersion;

	@Column(name = "migration_declined")
	private Boolean migrationDeclined = false;
}

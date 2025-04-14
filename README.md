# BudgetMaster
Manage your monthly budget easily with BudgetMaster

- __start:__ 17.12.16
- __current release:__ v2.18.0 (50) from 14.04.25

## Key Features
- Keep your data private - Host your own BudgetMaster server or use it in standalone mode. All data remains on your machines.
- Track transactions - Track your incomes and expenditures in transactions with name, description, tags, etc.
- Schedule transactions - Repeat recurring transactions automatically.
- Transaction Templates - Prepare templates for transactions that always look the same but are not repeated.
- Group Templates - Group your templates for a better overview.
- Multi Bank - Organize your transactions into multiple accounts.
- Categories - Group your transactions into categories.
- Data Backup - Backup your complete database or generate month reports as PDF.
- Modern UI - Responsive material design featuring light and dark mode.
- Password protected website - Your data can only be accessed by entering the correct password. (Note: The database is not encrypted)
- Localization - English and German supported.
- Search and Filter - Search for individual transactions or filter your view.
- Visualize your data - Use one of the pre-defined charts or create your one by using the chart framework to visualize and analyze your data.
- Auto Backup - Schedule an automatic export of your database content (as JSON export or as commit to a local or remote git repository).
- Icons - Assign icons to accounts, categories and templates.
- Create transactions based on bank documents - Import CSV files from your bank and directly create a transaction for each entry.

## Available Languages
- English [Roadmap](https://roadmaps.thecodelabs.de/roadmap/2)
- German [Roadmap](https://roadmaps.thecodelabs.de/roadmap/1)

## In case of Errors
- see corresponding error log `your_home_directory/Deadlocker/BudgetMaster/error.log`

## [Installation Instructions](https://github.com/deadlocker8/BudgetMaster/wiki/Installation)

## Screenshots

### [Light Theme](https://github.com/deadlocker8/BudgetMaster/wiki/Screenshots-Light-Theme)

![light_theme_home](/build/screenshots/light/home.png)

### [Dark Theme](https://github.com/deadlocker8/BudgetMaster/wiki/Screenshots-Dark-Theme)

![dark_theme_home](/build/screenshots/dark/home.png)

### Build from source

`mvn package -f pom.xml`

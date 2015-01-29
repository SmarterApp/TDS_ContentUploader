# Welcome to the ContentUploader Application

The ContentUploader allows the following functions:

* Upload the content file exported from Item Authoring (e.g. ITS) in ZIP format to the server file system.
* Publish the content to the Server in appropriate file structure as configured in the itembank db.
* Update the content size.
* Delete the Extraction folder or zip file.


## License ##
This project is licensed under the [AIR Open Source License v1.0](http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf).

## Getting Involved ##
We would be happy to receive feedback on its capabilities, problems, or future enhancements:

* For general questions or discussions, please use the [Forum](http://forum.opentestsystem.org/viewforum.php?f=9).
* Use the **Issues** link to file bugs or enhancement requests.
* Feel free to **Fork** this project and develop your changes!

## Module Overview

### Web

   Web module contains all the UI and implementation logic required for uploading and publishing content.


## Setup
In general, build the code and deploy the WAR file.

### Environment Variable (Tomcat Configuration)
The following parameters needs to be set inside context.xml of tomcat server.
```
<Parameter name="uploadPath"  override="false" value="<upload_folder_filesystem_location>"/>
<Parameter name="extractPath"  override="false" value="<extract_folder_filesystem_location>"/>
<Parameter name="tdscore.itembank"  override="false" value="User ID=<username>;password=<password>;Initial Catalog=<schema>;Data Source=localhost:3306;Connect Timeout=60;url=jdbc:mysql://<database-url>/<itembank_schemaname>/>

```


### Build order

If building all components from scratch the following build order is needed:

* sharedmultijardev
* tdsdlldev
* ItemRendererDev


## Dependencies
ContentUploaderDev has a number of direct dependencies that are necessary for it to function.  These dependencies are already built into the Maven POM files.

### Compile Time Dependencies

* shared-config
* shared-common
* shared-db
* shared-web
* item-renderer
* tds-dll-schemas
* commons-lang3
* tomahawk20
* xalan
* servlet-api
* logback-classic
* jcl-over-slf4j


### Runtime Dependency

* servlet-api

### Test Dependencies
* junit
* shared-test
* shared-db-test
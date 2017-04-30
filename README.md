# JSON validation service

 REST-service for validating JSON documents against JSON Schemas.
 
 ### Prerequisities
 * Apache Tomcat 7.0+
 * Eclipse Kepler IDE
 
 ### Build
 * After install the Eclipse Kepler IDE, necessary configurations can be made like showed in the link. [https://crunchify.com/step-by-step-guide-to-setup-and-install-apache-tomcat-server-in-eclipse-development-environment-ide/]
 * If project wanted to be served without using Eclipse, .war file should be created.    [ Right click project name in the Eclipse -> Export -> WAR file ] Created .war file should be placed in the webapps folder under the directory which is created after extracting the Apache Tomcat. Change the .war name to ROOT.war in order to serve project as default project. 
 * config.properties file must be placed under the /opt/json-validator folder. Only attribute of this config file is schemaFilesPath which is schema files storing directory. 
 Example config.properties file is liked that:

```
schemaFilesPath=/home/user/temp/jsonSchemaFiles 
```  
 
### Endpoints

```
POST    /schema/SCHEMAID        - Upload JSON Schema with unique `SCHEMAID`
GET     /schema/SCHEMAID        - Download JSON Schema with unique `SCHEMAID`

POST    /validate/SCHEMAID      - Validate JSON document against the JSON Schema identified by `SCHEMAID`
```
### Use case 

The potential user has a configuration JSON file `config.json` like the following:

```json
{
  "source": "/home/alice/image.iso",
  "destination": "/mnt/storage",
  "timeout": 32
}
```

And expects it conforms to the following JSON Schema `config-schema.json`:

```json
{
  "type": "object",
  "properties": {
    "source": {
      "type": "string"
    },
    "destination": {
      "type": "string"
    },
    "timeout": {
      "type": "integer",
      "minimum": 0,
      "maximum": 32767
    }
  },
  "required": ["source", "destination"]
}
```

To check that it really fits the schema:

1. Upload the JSON Schema: `curl http://localhost/schema/config-schema -X POST -d @config-schema.json`
2. Response will be: `{"action": "uploadSchema", "id": "config-schema", "status": "success"}` and status code 201
3. Upload the JSON document to validate it `curl http://localhost/validate/config-schema -X POST -d @config.json`
4. Response will be: `{"action": "validateDocument", "id": "config-schema", "status": "success"}` and status code 200


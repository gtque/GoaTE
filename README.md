GoaTE<br/>
This project is a monolith code base so do not be scared. GoaTE is broken down into modules, which may depend on other GoaTE modules. Each module is independently publishable. GoaTE extends and wraps existing libraries and frameworks to make test automation easier, more flexible, more robust, and just more awesome. There are also some custom frameworks introduced in GoaTE that add some kick-ass functionality. Documentation is going to be fairly light. Your best bet will be to look at the automated tests that are written. Hopefully the code will also be documented well enough to produce useful javadocs. The best way to use GoaTE is to pull it down from jcenter, please see the published artifacts to determine which version you need.<br/>
The typical pattern will be goate:module_namejava_version:major.minor.buildstamp<br/>
You can pull down only the modules you need to keep the foot print as small as possible.<br/>
<br/>
GoaTE follows the MIT License and we try to make sure we are in line with any licenses used by third party libraries. If you want to contribute to GoaTE, please feel free to branch or fork the project. Just be sure to maintain credit and licenses.
<br/>
<br/>

./gradlew -x gradle:publish publish -Prelease=final

./gradlew clean test testReport -PtestGroups=unit,api,ui,webui

./gradlew jacocoMergedReport

Good Luck.

ToDo:
* specify packages to scan
* completely remove org.atteo dependency
* example project using dependency jars
  * make sure tests and annotation factory still work.


* https://central.sonatype.org/publish/generate-portal-token/
* https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/
* https://central.sonatype.com/publishing/deployments
* https://ossrh-staging-api.central.sonatype.com/swagger-ui/#/default/manual_upload_repository
* https://central.sonatype.com/search

need to add a post call to "publish" after publishing.
- curl -X 'GET' \
  'https://ossrh-staging-api.central.sonatype.com/manual/search/repositories?ip=any' \
  -H 'accept: application/json' \
  -H "Authorization: Bearer ${SONATYPE_AUTH}" -i
- curl -X 'POST' \
  'https://ossrh-staging-api.central.sonatype.com/manual/upload/repository/qMyxIq%2F108.226.71.114%2Fcom.thegoate--default-repository?publishing_type=automatic' \
  -H 'accept: */*' \
  -H "Authorization: Bearer ${SONATYPE_AUTH}" \
  -d ''
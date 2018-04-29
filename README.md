To assist privacy impact assessments, this project takes the ico.org.uk's Privacy Impact Assessment and turns them into a database fronted by an AVPR (bit like REST) and GraphQL representation, with storage in MySQL or Postgres.

# Development

## CI
* Depends on a kubernetes setup with helm. From there concourse needs to be setup with a yaml file including the appropriate config for kubernetes and github.
* Tested and runs in Minikube... this pattern is quite nice and allows any dev to be the CI... albeit with the security risks that then exposes, a great idea for greenfield pre-production, but should be possible to secure into a cloud too.

## Backend (chat with Mark)
* AVRO AVPR for service and resource definition (bit like Swagger bit simpler and unique to AVRO dataformat)
* GraphQL for abstraction preferred for our frontend.
* Postgres or Mysql database is an option

## Frontend (chat with James)
* React, Typescript, Apollo, Styled Components.

# Flaws!
There is definitely at least one flaw with the current schema, where some of the free text field in the privacy impact assessment should be replaced with a table style structure.
If you wish to attempt to use this, then please fill out an assessment first using the annexes (https://ico.org.uk/for-organisations/guide-to-data-protection/privacy-by-design/) and then check to ensure the project captures your need.

# Copyright
The files derived from the ICO's assessment may likely be the copyright of the UK Govenment under the Open Government Licence.
http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/

The remainder, unless stated otherwise is only available for personal, non-commerical and one time use evalutation.

Copyrights, unless already stated, are all rights reserved to Mark Richards and James McAllister.
You are welcome to be inspired by them, but please contact us if you wish to copy them.


RedPhone-DCS
=================

Call metrics data collection server for RedPhone.  This is a JAX-RS servlet for collecting call quality metrics from Redphone clients and the Redphone switching server.

Two DataStore implementations are provided for persisting the data.  One use the local disk, and is useful for testing.  The second uses Amazon S3 and is used for the production deploy.  This project can be built with maven and run with jetty-runner, allowing simple deploys on Heroku.

Related
-----------------

For the related RedPhone secure VoIP project see:

https://github.com/whispersystems/redphone
https://www.whispersystems.org

License
---------------------

Copyright 2013 Whisper Systems

Licensed under the GPLv3: http://www.gnu.org/licenses/gpl-3.0.html



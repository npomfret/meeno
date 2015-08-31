meeno
=====

A strongly typed, immutable, java 8 implementation of the Betfair NG-API for use in non-interactive (aka bot) applications.

This project is functional but is still very much a work in progress... use at your own risk.  Most API calls have been implemented but not all.
If you need help please contact me via [github](https://github.com/npomfret) or [twitter](https://twitter.com/nickpomfret).

To get started take a look at the [live](https://github.com/npomfret/meeno/tree/master/test/live) examples.

There is no release as yet, but you can clone the project and run:

    gradle jar
    
Getting started
===============

* Get an app-key: https://api.developer.betfair.com/services/webapps/docs/display/1smk3cen4v3lu3yomq5qye0ni/API-NG+-+Visualiser
* Create a certificate: https://api.developer.betfair.com/services/webapps/docs/display/1smk3cen4v3lu3yomq5qye0ni/Non-Interactive+%28bot%29+login

```Shell
    cp /System/Library/OpenSSL/openssl.cnf .
    
    vi openssl.cnf
    # append the following text:
        [ ssl_client ]
        basicConstraints = CA:FALSE
        nsCertType = client
        keyUsage = digitalSignature, keyEncipherment
        extendedKeyUsage = clientAuth
    
    openssl genrsa -out client-2048.key 2048
    
    openssl req -new -config openssl.cnf -key client-2048.key -out client-2048.csr
    
    openssl x509 -req -days 365 -in client-2048.csr -signkey client-2048.key -out client-2048.crt -extfile openssl.cnf -extensions ssl_client
```
* Upload the certificate to https://myaccount.betfair.com/account/authentication?showAPI=1
* Enter the location of your certificate into the credentials.properties file 
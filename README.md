## DEVICE COLLECTOR API

## Summary
<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [DEVICE-COLLECTOR-API](#DEVICE-COLLECTOR-API)
  - [Context](#Context)
  - [Description](#Description)
  - [Routes](#Routes)
  - [Pipeline](#Pipeline)
  - [Problems](#Problems)
  - [Solutions](#Solutions)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Context
Each device (computer, smartphone, smart TV...) playing a Streamroot-powered video sends statistics to our data pipeline. That data is sent by each device through HTTP as **JSON payloads**. Each device sends one payload **every 30 seconds**.
The content of each payload is described below : 
```
{
    "token" : "c98arf53-ae39-4c9d-af44-c6957ee2f748",
    "customer": "Customer1",
    "content": "channel1",
    "timespan": 30000,
    "p2p": 560065,
    "cdn": 321123,
    "sessionDuration": 120000,
}
```
## Description
This API receive Http Json Payload, store and aggregate in cache, and then using a schedular, send data to Database each 5 minutes

                                     [POSTGRES]
                                          ⋀                                       
                                          ⋁              
                              **************************
                              *                        *
              [device]   ->   *  DEVICE-COLLECTOR-UDS  * -> [response]
                              *                        *
                              **************************
              -> request/response
              < >: external calls

## Routes
### Technical Route
- `/_healthcheck` : Check if API is running.

### API V1
- `/api/v1/stats` : Receive data, aggregate, cache it in memory and send it to DB each 5 minutes


## Pipeline
To launch pipeline : ./pipeline.sh

```yaml
pipeline:
  1- test application
  2- build and assembly RPM
  3- build image
  4- launch API and DB
```


## Problems
This library contains services used in both uds stream and uds api projects


### AMAZON S3 Service
- Store File in PANAMA
- Download File from PANAMA
- Delete File from PANAMA

### ElasticSearch Service
- Index Metadata in ES
- Update Metadata in ES
- Get Metadata from ES

## Solutions

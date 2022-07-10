export type AmplifyDependentResourcesAttributes = {
    "function": {
        "todo": {
            "Name": "string",
            "Arn": "string",
            "Region": "string",
            "LambdaExecutionRole": "string"
        }
    },
    "auth": {
        "testrestapi": {
            "IdentityPoolId": "string",
            "IdentityPoolName": "string",
            "UserPoolId": "string",
            "UserPoolArn": "string",
            "UserPoolName": "string",
            "AppClientIDWeb": "string",
            "AppClientID": "string"
        }
    },
    "api": {
        "api": {
            "RootUrl": "string",
            "ApiName": "string",
            "ApiId": "string"
        }
    }
}
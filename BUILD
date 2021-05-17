java_binary(
    name = "PC",
    srcs = glob(["src/*.java"]),
    deps = [":java_test_deps"]
)

java_binary(
    name = "MongoDocker",
    srcs = glob(["src/MongoDocker.java", "src/HostPort.java", "src/MongoDbParams.java"]),
    deps = [":java_test_deps"]
)

java_library(
    name = "java_test_deps",
    exports = [
        "@maven//:org_jsoup_jsoup",
        "@maven//:org_mongodb_mongo_java_driver",
    ],
)

.PHONY: all build clean average throughput singleshot sample report

MVN := ./mvnw
VERSION := $(shell xmllint --xpath "/*[local-name()='project']/*[local-name()='version']/text()" pom.xml 2>/dev/null || grep -m1 '<version>' pom.xml | sed -E 's/.*<version>(.*)<\/version>.*/\1/')
BENCH_JAR=reflectmap-benchmarks/target/reflectmap-benchmarks-$(VERSION).jar
RESULTS_DIR := reflectmap-benchmarks/results
BENCH_CLASS := ModelMapperBenchmark

all: build

build:
	$(MVN) clean package

reflectmap: build
	java -jar $(BENCH_JAR) \
		-rf json \
		-rff $(RESULTS_DIR)/reflectmap/all-metrics.json \
		-f1	\
		-t1	\
		-foe true \
		".*ReflectMapBenchmark.*" \
		$(ARGS)

modelmapper: build
	java -jar $(BENCH_JAR) \
		-rf json \
		-rff $(RESULTS_DIR)/modelmapper/all-metrics.json \
		-f1	\
		-t1	\
		-foe true \
		".*ModelMapperBenchmark.*" \
		$(ARGS)

clean:
	$(MVN) clean
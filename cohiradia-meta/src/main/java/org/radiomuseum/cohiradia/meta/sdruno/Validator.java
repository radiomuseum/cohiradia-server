package org.radiomuseum.cohiradia.meta.sdruno;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.radiomuseum.cohiradia.meta.yaml.YamlRepository;

import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    public List<Failure> validate(File sdrUnoFile, File yamlMetadata) {

        var headers = SdrUnoHeaders.create(sdrUnoFile);
        long fileSize = sdrUnoFile.length();
        var riff = headers.riff();
        var data = headers.data();
        var auxi = headers.auxi();
        var fmt = headers.fmt();

        List<Failure> failures = new ArrayList<>();

        var expectedRiffSize = fileSize - 8;
        check(failures, "riff.filesize", riff.filesize, expectedRiffSize);

        var expectedDataSize = fileSize - SdrUnoHeaders.HEADER_SIZE;
        check(failures, "data.filesize", data.fileSize(), expectedDataSize);

        var expectedNextFileName = "";
        check(failures, "auxi.nextFileName", auxi.nextfilename(), expectedNextFileName);
        //check(failures, "fmt.nSamplesPerSec", fmt.nSamplesPerSec(), 0);
        check(failures, "fmt.nAvgBytesPerSec", fmt.nAvgBytesPerSec(), fmt.nSamplesPerSec() * 4L);

        if (yamlMetadata != null && yamlMetadata.exists()) {
            var metaData = new YamlRepository().read(yamlMetadata);
            check(failures, "auxi.centerFrequency", auxi.centerFreq(), metaData.centerFrequencyAsHz());
            check(failures, "auxi.startTime", auxi.startTime().toString(), metaData.getRecordingDate().toString());
            check(failures, "auxi.stopTime", auxi.stopTime().toString(), metaData.getRecordingDate().plus((data.fileSize() / 4) / (fmt.nSamplesPerSec() / 1000), ChronoUnit.MILLIS).toString());
        }
        return failures;
    }

    private void check(List<Failure> failures, String property, long actual, long expected) {
        check(failures, property, Long.toString(actual), Long.toString(expected));
    }

    private void check(List<Failure> failures, String property, String actual, String expected) {
        if (!actual.equals(expected)) {
            failures.add(Failure.builder()
                    .property(property)
                    .actual(actual)
                    .expected(expected)
                    .build());
        }
    }

    @Builder
    @Data
    @Accessors(fluent = true)
    public static class Failure {

        private String property;
        private String expected;
        private String actual;

        public String difference() {
            try {
                double expected = Double.parseDouble(expected());
                double actual = Double.parseDouble(actual());
                return Double.toString(expected - actual);
            } catch (RuntimeException e) {
                return "-";
            }
        }

        public String toString() {
            return String.format("%s: actual=%s, expected=%s, diff=%s", property, actual, expected, difference());
        }

    }

}

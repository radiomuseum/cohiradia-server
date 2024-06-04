package org.radiomuseum.cohiradia.meta.yaml;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MetaData {

    @JsonProperty(value = "id", defaultValue = "0")
    private int id;
    @JsonProperty(value = "uri", required = true)
    private String uri;
    @JsonProperty(value = "recording-date", required = true)
    private ZonedDateTime recordingDate;
    @JsonProperty(value = "duration", required = true)
    private int duration;
    @JsonProperty(value = "band", required = true)
    private String band;
    @JsonProperty(value = "frequency-unit", required = true)
    private String frequencyUnit;
    @JsonProperty(value = "frequency-low", required = true)
    private double frequencyLow;
    @JsonProperty(value = "frequency-high", required = true)
    private double frequencyHigh;
    @JsonProperty(value = "frequency-correction")
    private double frequencyCorrection;
    @JsonProperty(value = "encoding", required = true)
    private String encoding;
    @JsonProperty(value = "center-frequency", required = true)
    private double centerFrequency;
    @JsonProperty(value = "bandwidth", required = true)
    private double bandwidth;
    @JsonProperty(value = "antenna")
    private String antenna;
    @JsonProperty(value = "recording-type")
    private String recordingType;
    @JsonProperty(value = "remark")
    private String note;
    @JsonProperty(value = "content")
    private String content;
    @JsonIgnore
    private transient String radioStation;
    @JsonProperty(value = "radio-stations")
    private List<RadioStation> radioStations;
    @JsonProperty(value = "radio-station-announcement")
    private String radioStationAnnouncement;
    @JsonProperty(value = "location-longitude")
    private String locationLongitude;
    @JsonProperty(value = "location-latitude")
    private String locationLatitude;
    @JsonProperty(value = "location-country")
    private String locationCountry;
    @JsonProperty(value = "location-city")
    private String locationCity;
    @JsonProperty(value = "location-qth")
    private String locationQTH;
    @JsonProperty(value = "upload-user-fk")
    private int uploadUserFk;
    @JsonProperty(value = "filters")
    private String filters;
    @JsonProperty(value = "preamp-settings")
    private String preampSettings;
    @JsonProperty(value = "author")
    private String author;
    @JsonProperty(value = "software")
    private String software;

    @JsonIgnore
    public int getLocationCountryFk() {
        if(getLocationCountry() == null){
            return 0;
        }

        return switch (getLocationCountry().toUpperCase()) {
            case "AT" -> 2;
            case "US" -> 3;
            case "CA" -> 4;
            case "AU" -> 5;
            case "MX" -> 6;
            case "JP" -> 7;
            case "CH" -> 8;
            case "NL" -> 9;
            case "FR" -> 10;
            case "CN" -> 13;
            case "TW" -> 14;
            case "PH" -> 16;
            case "GB" -> 17;
            case "NZ" -> 18;
            case "ES" -> 19;
            case "IT" -> 20;
            case "AF" -> 100;
            case "AL" -> 101;
            case "AO" -> 103;
            case "AZ" -> 104;
            case "BE" -> 105;
            case "BD" -> 106;
            case "BB" -> 107;
            case "BF" -> 108;
            case "BG" -> 109;
            case "BZ" -> 110;
            case "BT" -> 111;
            case "BO" -> 112;
            case "BR" -> 113;
            case "BN" -> 115;
            case "BY" -> 117;
            case "CU" -> 118;
            case "CM" -> 119;
            case "CG" -> 120;
            case "LK" -> 122;
            case "CO" -> 123;
            case "KM" -> 124;
            case "CR" -> 125;
            case "CV" -> 126;
            case "CY" -> 127;
            case "CZ" -> 128;
            case "DK" -> 129;
            case "DO" -> 130;
            case "BJ" -> 131;
            case "DZ" -> 132;
            case "KE" -> 133;
            case "TZ" -> 134;
            case "UG" -> 135;
            case "EC" -> 136;
            case "ER" -> 137;
            case "SV" -> 138;
            case "EE" -> 139;
            case "EG" -> 140;
            case "ET" -> 141;
            case "FI" -> 142;
            case "FJ" -> 143;
            case "GA" -> 147;
            case "GT" -> 153;
            case "GE" -> 154;
            case "GH" -> 155;
            case "GW" -> 156;
            case "GQ" -> 157;
            case "GR" -> 158;
            case "GY" -> 159;
            case "HU" -> 160;
            case "HN" -> 161;
            case "HR" -> 162;
            case "IL" -> 163;
            case "IN" -> 164;
            case "IR" -> 165;
            case "IE" -> 166;
            case "IQ" -> 167;
            case "IS" -> 168;
            case "JM" -> 169;
            case "JO" -> 170;
            case "KH" -> 171;
            case "KG" -> 173;
            case "SA" -> 174;
            case "KW" -> 175;
            case "KZ" -> 176;
            case "LA" -> 178;
            case "LY" -> 179;
            case "LR" -> 180;
            case "LS" -> 181;
            case "LT" -> 182;
            case "LV" -> 183;
            case "MT" -> 184;
            case "MA" -> 185;
            case "MY" -> 186;
            case "MD" -> 188;
            case "MN" -> 189;
            case "MK" -> 191;
            case "MZ" -> 192;
            case "MU" -> 193;
            case "MV" -> 194;
            case "MW" -> 195;
            case "MM" -> 196;
            case "NO" -> 197;
            case "NA" -> 199;
            case "NP" -> 201;
            case "NI" -> 202;
            case "OM" -> 203;
            case "PT" -> 204;
            case "PA" -> 205;
            case "PE" -> 206;
            case "PK" -> 207;
            case "PL" -> 208;
            case "PG" -> 209;
            case "PY" -> 210;
            case "QA" -> 211;
            case "AR" -> 212;
            case "BW" -> 213;
            case "CF" -> 214;
            case "CL" -> 216;
            case "GN" -> 217;
            case "HT" -> 218;
            case "ID" -> 219;
            case "MR" -> 220;
            case "LB" -> 221;
            case "MG" -> 222;
            case "ML" -> 223;
            case "NE" -> 224;
            case "RO" -> 225;
            case "UY" -> 226;
            case "TG" -> 228;
            case "BI" -> 229;
            case "RU" -> 230;
            case "RW" -> 231;
            case "SE" -> 232;
            case "SZ" -> 233;
            case "SK" -> 235;
            case "SI" -> 236;
            case "SR" -> 237;
            case "SN" -> 238;
            case "SO" -> 239;
            case "ST" -> 240;
            case "SD" -> 242;
            case "SC" -> 243;
            case "SY" -> 244;
            case "TD" -> 245;
            case "TH" -> 246;
            case "TJ" -> 247;
            case "TM" -> 248;
            case "TN" -> 249;
            case "TR" -> 251;
            case "TT" -> 252;
            case "UA" -> 254;
            case "AE" -> 255;
            case "UZ" -> 256;
            case "VN" -> 258;
            case "VU" -> 259;
            case "GM" -> 260;
            case "SL" -> 261;
            case "NG" -> 262;
            case "DM" -> 263;
            case "GD" -> 264;
            case "LC" -> 265;
            case "YE" -> 268;
            case "VE" -> 270;
            case "ZM" -> 271;
            case "ZW" -> 273;
            case "DE" -> 1;
            case "ZA" -> 11;
            case "KR" -> 15;
            case "AM" -> 1003;
            case "RS" -> 1004;
            case "CD" -> 275;
            case "BA" -> 1007;
            case "ZZ" -> 1002;
            default -> 0;
        };
    }

    public long frequencyFactor() {
        return switch (getFrequencyUnit()) {
            case "kHz" -> 1000;
            case "Hz" -> 1;
            case "MHz" -> 1000 * 1000;
            default -> throw new IllegalStateException("Unknown unit.");
        };
    }

    @JsonIgnore
    public int centerFrequencyAsHz() {
        return (int) (frequencyFactor() * getCenterFrequency());
    }


}

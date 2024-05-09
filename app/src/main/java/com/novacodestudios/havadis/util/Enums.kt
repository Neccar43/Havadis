package com.novacodestudios.havadis.util


enum class SearchIn {
    TITLE,
    DESCRIPTION,
    CONTENT;

    override fun toString(): String {
        return when (this) {
            TITLE -> "title"
            DESCRIPTION -> "description"
            CONTENT -> "content"
        }
    }
}

enum class Category(val value: String) {
    GENERAL("general"),
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");

    companion object {
        fun fromCategoryValue(value: String?): Category? {
            return Category.entries.find { it.value == value }
        }
    }
}

enum class Country(val countryCode: String) {
    ARGENTINA("ar"),
    AUSTRALIA("au"),
    AUSTRIA("at"),
    BELGIUM("be"),
    BRAZIL("br"),
    BULGARIA("bg"),
    CANADA("ca"),
    CHINA("cn"),
    COLOMBIA("co"),
    CUBA("cu"),
    CZECH_REPUBLIC("cz"),
    EGYPT("eg"),
    FRANCE("fr"),
    GERMANY("de"),
    GREECE("gr"),
    HONG_KONG("hk"),
    HUNGARY("hu"),
    INDIA("in"),
    INDONESIA("id"),
    IRELAND("ie"),
    ISRAEL("il"),
    ITALY("it"),
    JAPAN("jp"),
    SOUTH_KOREA("kr"),
    LATVIA("lv"),
    LITHUANIA("lt"),
    MALAYSIA("my"),
    MEXICO("mx"),
    MOROCCO("ma"),
    NETHERLANDS("nl"),
    NEW_ZEALAND("nz"),
    NIGERIA("ng"),
    NORWAY("no"),
    PHILIPPINES("ph"),
    POLAND("pl"),
    PORTUGAL("pt"),
    ROMANIA("ro"),
    RUSSIA("ru"),
    SAUDI_ARABIA("sa"),
    SERBIA("rs"),
    SINGAPORE("sg"),
    SLOVAKIA("sk"),
    SLOVENIA("si"),
    SOUTH_AFRICA("za"),
    SWEDEN("se"),
    SWITZERLAND("ch"),
    TAIWAN("tw"),
    THAILAND("th"),
    TURKEY("tr"),
    UAE("ae"),
    UKRAINE("ua"),
    UNITED_KINGDOM("gb"),
    UNITED_STATES("us"),
    VENEZUELA("ve");

    companion object {
        fun fromCountryCode(code: String): Country? {
            return entries.find { it.countryCode == code }
        }
    }
}

enum class Language(val code: String) {
    ARABIC("ar"),
    GERMAN("de"),
    ENGLISH("en"),
    SPANISH("es"),
    FRENCH("fr"),
    HEBREW("he"),//
    ITALIAN("it"),
    DUTCH("nl"),//
    NORWEGIAN("no"),//
    PORTUGUESE("pt"),//
    RUSSIAN("ru"),
    SWEDISH("sv"),//
    TURKISH_GERMAN("ud"),//
    CHINESE("zh");//

    companion object {
        fun fromLanguageCode(code: String): Language? = entries.find { it.code == code }
    }
}

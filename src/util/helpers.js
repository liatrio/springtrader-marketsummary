const parser = require("accept-language-parser");
const LocaleCurrency = require("locale-currency");

const getLocaleFromRequest = (request) => {
    const [language] = parser.parse(request.headers["accept-language"]);

    if (!language) {
        return "en-US";
    }

    if (language.region) {
        return language.code + "-" + language.region;
    }

    return language.code;
};

const localeIsValid = (locale) => {
    const currencyCode = LocaleCurrency.getCurrency(locale);

    return currencyCode !== null;
};

module.exports = {
    getLocaleFromRequest,
    localeIsValid,
};

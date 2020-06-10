const parser = require("accept-language-parser");

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

module.exports = {
    getLocaleFromRequest,
};

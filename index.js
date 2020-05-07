const hapi = require("@hapi/hapi");

(async () => {
  const server = hapi.server({
    port: 5555,
    host: "0.0.0.0",
  });

  server.route({
    method: "GET",
    path: "/",
    handler: (request, h) => {
      return "hello world";
    },
  });

  await server.start();
  console.log("Server running on %s", server.info.uri);
})();

process.on("unhandledRejection", (err) => {
  console.log(err);
  process.exit(1);
});

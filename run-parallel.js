const { spawn } = require("child_process");

const args = process.argv.slice(2);
let numProcesses = 1;
if (args.length > 0) {
  numProcesses = args[0];
}

console.log(numProcesses);

// 各プロセスで実行するコマンド
const command = "newman run collection.json";

for (let i = 0; i < numProcesses; i++) {
  const process = spawn("sh", ["-c", command]);

  process.stdout.on("data", (data) => {
    console.log(`stdout: ${data}`);
  });

  process.stderr.on("data", (data) => {
    console.error(`stderr: ${data}`);
  });

  process.on("close", (code) => {
    console.log(`child process exited with code ${code}`);
  });
}

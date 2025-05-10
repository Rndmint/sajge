import os, argparse, paramiko

def main():
    p = argparse.ArgumentParser()
    p.add_argument("--ssh-host",    required=True)
    p.add_argument("--ssh-user",    required=True)
    p.add_argument("--ssh-pass",    required=True)
    p.add_argument("--remote-path", required=True)
    args = p.parse_args()

    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(args.ssh_host, username=args.ssh_user, password=args.ssh_pass)

    cmd = f"nohup java -jar {args.remote_path} > server.log 2>&1 &"
    ssh.exec_command(cmd)
    print(f"Started server at {args.remote-path}")

    ssh.close()


if __name__ == "__main__":
    main()

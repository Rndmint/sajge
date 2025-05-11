import argparse
import paramiko
from scp import SCPClient

def main():
    p = argparse.ArgumentParser()
    p.add_argument("--ssh-host",    required=True)
    p.add_argument("--ssh-user",    required=True)
    p.add_argument("--ssh-pass",    required=True)
    p.add_argument("--jar",         required=True)
    p.add_argument("--remote-path", required=True)
    args = p.parse_args()

    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(args.ssh_host, username=args.ssh_user, password=args.ssh_pass)

    with SCPClient(ssh.get_transport()) as scp:
        scp.put(args.jar, args.remote_path)
        print(f"Copied {args.jar} -> {args.remote_path}")

    ssh.close()

if __name__ == "__main__":
    main()

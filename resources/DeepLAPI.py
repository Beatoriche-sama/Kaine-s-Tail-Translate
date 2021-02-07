import time
import json
from operator import itemgetter
import sys
sys.path.insert(0, sys.argv[6])
import requests

text = sys.argv[2]
language = sys.argv[4]

url = "https://www2.deepl.com/jsonrpc"

r = requests.post(
    url,
    json = {
        "jsonrpc":"2.0",
        "method": "LMT_handle_jobs",
        "params": {
            "jobs":[{
                "kind":"default",
                "raw_en_sentence": text,
                "raw_en_context_before":[],
                "raw_en_context_after":[],
                "preferred_num_beams":4,
                "quality":"fast"
            }],
            "lang":{
                "source_lang_user_selected":"auto",
                "target_lang": language
            },
            "priority":-1,
            "commonJobParams":{},
                            "timestamp": int(round(time.time() * 1000))
        },
        "id": 40890008
    }
)
k = json.dumps(r.json())
print(k)
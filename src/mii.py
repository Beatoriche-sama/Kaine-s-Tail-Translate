import requests
import time
import json
from operator import itemgetter

url = "https://www2.deepl.com/jsonrpc"
text = "Hello, how are you today?"

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
                "user_preferred_langs":["FR","EN"],
                "source_lang_user_selected":"auto",
                "target_lang":"FR"
            },
            "priority":-1,
            "commonJobParams":{},
            "timestamp": int(round(time.time() * 1000))
        },
        "id": 40890008
    }
)

results = json.loads(r.text)['result']
getFields = itemgetter('postprocessed_sentence')

for translation in results['translations']:
    for variant in translation.get('beams', []):
        print(''.join(map(str, getFields(variant))))
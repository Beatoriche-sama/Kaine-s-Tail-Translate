import sys
sys.path.insert(0, sys.argv[6])
from googletrans import Translator
translator = Translator()
text = sys.argv[2]
language = sys.argv[4]
result = translator.translate(text, dest=language)

print(result.text)
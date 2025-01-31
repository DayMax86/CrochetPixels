Takes the pixel data from an 80x80 image and formats it in a crochet-friendly way.

Here is the Python code to generate the JSON:

```from PIL import Image
import json

img = Image.open('entertainment.png')
pix = img.load()
print(img.size)

colour = ""
uniqueColours = []
coords = []

#Make a JSON file based on the unique colours
datum = {}
data = []
i = 0

for y in range(80):
    for x in range(80):
        
        colour = img.convert('RGB').getpixel((x,y))
        coord = {
        "x": x,
        "y": y,
        "colour": colour
        }
        
        datum = (coord)
        
        if colour not in uniqueColours:
                  uniqueColours.append(colour)
        
        data.append(datum)
    
print(uniqueColours)

j = json.dumps(data)
with open('data.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, ensure_ascii=False, indent=4)
```

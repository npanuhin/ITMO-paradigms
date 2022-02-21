import os
import re

HOME = "../"
EXTENSIONS = (".java", ".py", ".cpp", ".c" ".cmd")


def mkpath(*paths):
    return os.path.normpath(os.path.join(*paths))


for path, folders, files in os.walk(mkpath(HOME)):
    if path.startswith(mkpath(HOME, "tests")):
        continue

    for filename in files:
        if filename.endswith(EXTENSIONS):
            # print(mkpath(path, file))

            with open(mkpath(path, filename), 'r', encoding="utf-8") as file:
                content = file.read()

            # Replace tabs with spaces:
            content = content.replace('\t', ' ' * 4)

            # Remove spaces and the end of lines
            content = re.sub(r"([\t ]+)\n", "\n", content)

            # Add newline at the end of file if missing:
            if not content.endswith('\n'):
                content += '\n'

            with open(mkpath(path, filename), 'w', encoding="utf-8") as file:
                file.write(content)

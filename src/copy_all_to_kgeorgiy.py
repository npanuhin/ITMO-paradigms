import shutil
import os


ROOT = "../"
KGEORGIY_ROOT = "../../paradigms/"


def mkpath(*paths):
    return os.path.normpath(os.path.join(*paths))


def removeFromKgeorgiy(path):
    if os.path.isfile(mkpath(KGEORGIY_ROOT, path)):
        os.remove(mkpath(KGEORGIY_ROOT, path))

    elif os.path.isdir(mkpath(KGEORGIY_ROOT, path)):
        shutil.rmtree(mkpath(KGEORGIY_ROOT, path))


def ext(path):
    return os.path.splitext(mkpath(path))[1]


def listdir(*paths):
    return os.listdir(mkpath(ROOT, *paths))


def listdir_java(*paths):
    for file in listdir(*paths):
        if ext(file) == ".java":
            yield file


def copy(src, dst):
    src = src if isinstance(src, str) else mkpath(*src)
    dst = dst if isinstance(dst, str) else mkpath(*dst)
    print("Copying {} to {}".format(src, dst))
    src = mkpath(ROOT, src)
    dst = mkpath(KGEORGIY_ROOT, dst)

    if not os.path.splitext(dst)[1].strip():
        dirname = dst
        dst = mkpath(dst, os.path.split(src)[1])
    else:
        dirname = os.path.dirname(dst)

    os.makedirs(dirname, exist_ok=True)

    shutil.copyfile(src, dst)


def copytree(src, dst, preserve=True):
    src = src if isinstance(src, str) else mkpath(*src)
    dst = dst if isinstance(dst, str) else mkpath(*dst)
    print("Copying {} to {}".format(src, dst))
    src = mkpath(ROOT, src)
    dst = mkpath(KGEORGIY_ROOT, dst)

    # if os.path.isdir(dst):
    #     shutil.rmtree(dst)

    shutil.copytree(src, dst, dirs_exist_ok=True)


def main():
    removeFromKgeorgiy("java-solutions")

    copytree("MyClasses", ("java-solutions", "myclasses"))

    copytree(("HW1", "expression"), ("java-solutions", "expression"))

    copytree(("HW2", "search"), ("java-solutions", "search"))

    copytree(("HW3", "queue"), ("java-solutions", "queue"))

    copytree(("HW4", "queue"), ("java-solutions", "queue"))

    copytree(("HW5", "expression"), ("java-solutions", "expression"))

    [copy(("HW5", "Main.java"), ("java-solutions"))]
    [copy(("src", "Question.java"), ("java-solutions"))]


if __name__ == "__main__":
    main()

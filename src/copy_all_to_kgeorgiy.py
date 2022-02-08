from shutil import copyfile, rmtree
import os


ROOT = "../"
KGEORGIY_ROOT = "../../paradigms/"


def mkpath(*paths):
    return os.path.normpath(os.path.join(*paths))


def removeFromKgeorgiy(path):
    if os.path.isfile(mkpath(KGEORGIY_ROOT, path)):
        os.remove(mkpath(KGEORGIY_ROOT, path))

    elif os.path.isdir(mkpath(KGEORGIY_ROOT, path)):
        rmtree(mkpath(KGEORGIY_ROOT, path))


def ext(path):
    return os.path.splitext(mkpath(path))[1]


def listdir(*paths):
    return os.listdir(mkpath(ROOT, *paths))


def listdir_java(*paths):
    for file in listdir(*paths):
        if ext(file) == ".java":
            yield file


def copy(src, dst):
    print("Copying {} to {}".format(src, dst))
    src = mkpath(ROOT, src if isinstance(src, str) else mkpath(*src))
    dst = mkpath(KGEORGIY_ROOT, dst if isinstance(dst, str) else mkpath(*dst))

    if not os.path.splitext(dst)[1].strip():
        dirname = dst
        dst = mkpath(dst, os.path.split(src)[1])
    else:
        dirname = os.path.dirname(dst)

    os.makedirs(dirname, exist_ok=True)

    copyfile(src, dst)


def main():
    removeFromKgeorgiy("java-solutions")

    [copy(("MyClasses", file), ("java-solutions", "myclasses")) for file in listdir_java("MyClasses")]

    [copy(("HW1", "expression", file), ("java-solutions", "expression")) for file in listdir_java("HW1", "expression")]
    [copy(("HW1", "expression", "exceptions", file), ("java-solutions", "expression", "exceptions")) for file in listdir_java("HW1", "expression", "exceptions")]
    [copy(("HW1", "expression", "parser", file), ("java-solutions", "expression", "parser")) for file in listdir_java("HW1", "expression", "parser")]

    [copy(("HW2", "search", file), ("java-solutions", "search")) for file in listdir_java("HW2", "search")]

    [copy(("HW1", "Main.java"), ("java-solutions"))]


if __name__ == "__main__":
    main()

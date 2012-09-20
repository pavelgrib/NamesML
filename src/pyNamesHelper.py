import random, os, math, getpass
from string import ascii_letters

namesPath = '/Users/' + str(getpass.getuser()) + '/github/local/NamesML/names/'
trainingPath = '/Users/' + str(getpass.getuser()) + '/github/local/NamesML/training/'
validatePath = '/Users/'+ str(getpass.getuser()) + '/github/local/NamesML/validation/'

def extractTrainingSet(file, percent):
    fd_in = open(namesPath + file, 'r')
    fd_train = open(trainingPath + file, 'w')
    fd_validate = open(validatePath + file, 'w')
    train_count = 0
    validate_count = 0
    for line in fd_in:
        name = line.split('\t')[0].lower()
        if random.uniform(0,100) < percent:
            fd_train.write(name + '\n')
            train_count = train_count + 1
        else:
            fd_validate.write(name + '\n')
            validate_count = validate_count + 1
    fd_train.flush()
    fd_train.close()
    fd_validate.flush()
    fd_validate.close()
    print 'written training file containing ' + str(train_count) + ' names to ' + trainingPath + file
    print 'written validation file containing ' + str(validate_count) + ' names to ' + validatePath + file

## quickly pick some elements and manually determine spelling of those entries to estimate p(C)
def randomEntries(n, file):
    fd = open(file, 'r')
    lines = fd.readlines()
    entries = map(lambda u: u.split('\t')[0], random.sample(lines, n))
    output = dict()
    for entry in entries:
        flag = int(raw_input("is " + entry + " misspelled? "))
        output[entry] = flag
    print float(sum(output.values())) / float(len(output))
    return output

def strHist(file, n):
    fd = open(fle, 'r')
    hist = dict()
    lines = "".join(rd.readlines())
    

def charHist(file):
    fd = open(file, 'r')
    hist = dict()
    lines = ''.join(fd.readlines())
    for c in xrange(ord('a'),ord('z')+1):
        hist[chr(c)] = lines.count(chr(c))
    fd.close()
    return hist

def printHist(hist):
    m = hist[max(hist, key=hist.get)]
    for k, v in hist.items():
        rep = int(100.0*float(v)/float(m))
        print str(k) + ' ' + ('|' * rep)

def combineFilesList(filesList, newFile):
    out = open(newFile,'w+')
    for i in filesList:
        fd = open(i,'r')
        out.write(''.join(fd.readlines()))
    out.flush()
    out.close()

def combineFiles(file1, file2, newFile):
    fd1 = open(file1, 'r+')
    fd2 = open(file2, 'r+')
    out = open(newFile,'w+')
    lines1 = map(lambda s: s.lower(), fd1.readlines())
    lines2 = map(lambda s: s.lower(), fd2.readlines())
    out.write(''.join(lines1))
    out.write(''.join(lines2))
    out.flush()
    out.close()

def splitFile(file, times):
    fd = open(file, 'r')
    lines = fd.readlines()
    linesperfile = int(math.ceil(float(len(lines)) / float(times)))
    name = fd.name.split('/')[-1]
    count = 0
    for i in range(0,times):
        out = open('/'.join(fd.name.split('/')[0:-1]) + '/' + name.split('.')[0] + str(i+1) + '.' + name.split('.')[1],'w+')
        out.write(''.join(lines[i*linesperfile:(i+1)*linesperfile]))
        out.flush()
        out.close()
        counter = open(out.name, 'r')
        count = count + len(counter.readlines())
        counter.close()
    print str(len(lines)) + ' lines in original and ' + str(count) + ' lines written out'
    fd.close()

if __name__ == '__main__':
    pass

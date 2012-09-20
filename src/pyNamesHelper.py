import random, os, math
from string import ascii_letters

namesPath = '/Users/pg/github/local/NamesML/names/'
trainingPath = '/Users/pg/github/local/NamesML/training/'
validatePath = '/Users/pg/github/local/NamesML/validation/'

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
    print len(entries)
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
    lines = "".join(fd.readlines())
    for c in xrange(ord('a'),ord('z')+1):
        hist[chr(c)] = lines.count(chr(c))
    fd.close()
    return hist

def printHist(hist):
    m = hist[max(hist, key=hist.get)]
    for k, v in hist.items():
        rep = int(100.0*float(v)/float(m))
        print str(k) + ' ' + ('|' * rep)

if __name__ == '__main__':
    pass

import random
import names
import string
import datetime
import csv

def getRandomFirstName():
    return names.get_first_name()

def getRandomLastName():
    return names.get_last_name()

def getRandomString(length):
    return ''.join(random.choice(string.ascii_letters) for i in range(length))

def getEmailFromNames(firstName, lastName):
    domain = getRandomString(3) + '.' + getRandomString(3)
    firstNameLetters = random.randint(1, len(firstName))
    lastNameLetters = random.randint(1, len(lastName))
    firstNameTruncated = firstName[:firstNameLetters]
    lastNameTruncated = lastName[:lastNameLetters]
    return firstNameTruncated + lastNameTruncated + '@' + domain

# phone number is typically 10 digits (xxx) xxx-xxxx
def getRandomPhoneNumber(length=10):
    return ''.join([str(random.randint(0, 9)) for i in range(length)])

# pass startDate and endDate as datetime.date values
# ex: getRandomDate(datetime.date(1900, 2, 5), datetime.date(2020, 8, 20))
def getRandomDate(startDate, endDate):
    dateOptions = []
    while startDate != endDate:
        dateOptions.append(startDate)
        startDate += datetime.timedelta(days=1)
    return random.choice(dateOptions)

def writeObjectsToCsv(objects, outputFileName, writeHeader=True):
    with open(outputFileName, 'w') as outputFile:
        csvWriter = csv.writer(outputFile)
        if writeHeader:
            csvWriter.writerow([key for key, value in objects[0].__dict__.items()])
        for object in objects:
            csvWriter.writerow([value for key, value in object.__dict__.items()])
    return True # if successfully wrote to the csv

if __name__ == '__main__':
    for i in range(100):
        print(getRandomDate(datetime.date(1900, 2, 5), datetime.date(2020, 9, 18)))
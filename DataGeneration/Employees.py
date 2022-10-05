import Utils
import random
import datetime
import numpy as np

class Employee:
    numberOfEmployees = 0

    def __init__(self):
        self.id = Employee.numberOfEmployees
        Employee.numberOfEmployees += 1

        self.firstName = Utils.getRandomFirstName()
        self.lastName = Utils.getRandomLastName()
        self.phone = Utils.getRandomPhoneNumber()
        self.email = Utils.getEmailFromNames(self.firstName, self.lastName)

        # randome options can be modified as needed
        self.isAdmin = random.choice([False, True])
        self.hourlyRate = round(random.choice(np.arange(10, 30, .01)), 2)
        self.hoursPerWeek = round(random.choice(np.arange(1, 50, .1)), 2)
        self.hireDate = Utils.getRandomDate(datetime.date(1980, 1, 1), datetime.date(2022, 1, 1))
        self.endDate = Utils.getRandomDate(self.hireDate, datetime.date(2022, 2, 2))

        self.active = random.choice([False, True])
        # active employees shouldn't have an end date
        if self.active:
            self.endDate = ''

def generateEmployees(numToGenerate):
    return [Employee() for i in range(numToGenerate)]

# run main to just generate new employees
if __name__ == '__main__':
    employees = generateEmployees(10)
    Utils.writeObjectsToCsv(employees, 'data/Employees.csv')
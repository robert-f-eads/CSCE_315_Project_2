from typing import List
import Utils
import datetime
import random

class RewardMember:
    numberOfRewardsMembers = 0

    def __init__(self, defaultRewardMember=False):
        self.id = RewardMember.numberOfRewardsMembers
        RewardMember.numberOfRewardsMembers += 1

        self.firstName = ''
        self.lastName = ''
        self.phone = ''
        self.email = ''
        self.birthday = '' # date
        self.rewardPoints = 0

        if defaultRewardMember:
            self.makeDefaultRewardMember()
        else:
            self.randomizeFields()

    # we don't randomize id because we want it to be a sequential primary key
    def randomizeFields(self, phoneNumberLength=10):
        self.firstName = Utils.getRandomFirstName()
        self.lastName = Utils.getRandomLastName()
        self.phone = Utils.getRandomPhoneNumber()
        self.email = Utils.getEmailFromNames(self.firstName, self.lastName)
        self.birthday = Utils.getRandomDate(datetime.date(1900, 1, 1), datetime.date(2020, 1, 1)) # arbitrary start and end date limits
        self.rewardPoints = random.randint(0, 500) # 500 is arbitrary upper limit

    def makeDefaultRewardMember(self):
        self.firstName = 'Default'

def generateRewardsMembers(numToGenerate):
    generatedRewardsMembers: List[RewardMember] = []

    # if we want to create a default reward member
    # defaultRewardMember = RewardMember(defaultRewardMember=True)
    # generatedRewardsMembers.append(defaultRewardMember)

    for i in range(numToGenerate):
        newRewardMember = RewardMember()
        generatedRewardsMembers.append(newRewardMember)
    return generatedRewardsMembers

if __name__ == '__main__':
    rewardsMembers = generateRewardsMembers(100)
    Utils.writeObjectsToCsv(rewardsMembers, 'data/RewardsMembers.csv')
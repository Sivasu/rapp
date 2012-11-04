CREATE TABLE Staging (
	Name VARCHAR(50),
	ID VARCHAR(10),
	Company VARCHAR(30),
	Exp FLOAT(3,1),

	Source VARCHAR(20),
	JobStep VARCHAR(30),
	ReviewStepStartDate DATE,

	Reviewer1Name VARCHAR(50),
	Reviewer1StartDate DATE,
	Reviewer1EndDate DATE,
	Reviewer1Lang VARCHAR(10),
	Reviewer1Reco VARCHAR(10),

	Reviewer2Name VARCHAR(50),
	Reviewer2StartDate DATE,
	Reviewer2EndDate DATE,
	Reviewer2Lang VARCHAR(10),
	Reviewer2Reco VARCHAR(10),

	Tags VARCHAR(100)
);

--//@UNDO

DROP TABLE Staging;
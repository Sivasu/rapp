CREATE TABLE tbl_Review(
ID INT AUTO_INCREMENT PRIMARY KEY,
CandidateID INT,
ReviewerID INT,
Technology VARCHAR(10),
StartDate DATE,
EndDate DATE,
Result BIT,
FOREIGN KEY (CandidateID) REFERENCES tbl_Candidate(ID),
FOREIGN KEY (ReviewerID) REFERENCES tbl_Reviewer(ID)
);

--//@UNDO

DROP TABLE tbl_Review;
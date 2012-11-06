CREATE TABLE tbl_Candidate (
ID INT AUTO_INCREMENT PRIMARY KEY,
CandidateID VARCHAR(10) NOT NULL, 
Name VARCHAR (50), 
Source VARCHAR(20),
Experience FLOAT (3,1),
Company VARCHAR (30) );

--//@UNDO

DROP TABLE tbl_Candidate;
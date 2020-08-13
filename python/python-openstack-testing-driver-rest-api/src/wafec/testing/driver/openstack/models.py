from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, ForeignKey, DateTime, create_engine
from sqlalchemy.orm import sessionmaker, relationship
import datetime

Base = declarative_base()


class Key(Base):
    __tablename__ = 'PASS_KEY'

    id = Column(Integer, primary_key=True)
    pass_value = Column(String, name="pass", unique=True, nullable=False)
    username = Column(String)
    password = Column('passwd', String)
    project_name = Column(String)
    user_domain_name = Column(String)
    project_domain_name = Column(String)
    auth_url = Column(String)

    def __repr__(self):
        return '<Key(id=%s, pass_value=%s)>' % (
            self.id,
            self.pass_value
        )


engine = create_engine('mysql+pymysql://testing:testing-321@localhost/testing', pool_recycle=3600)
Session = sessionmaker(bind=engine)

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OpenQA.Selenium.Remote;
using OpenQA.Selenium;
using System.Collections.ObjectModel;

namespace TestRemote
{
    class RemoteWebDriverExtended : RemoteWebDriver
    {
        public RemoteWebDriverExtended(ICommandExecutor commandExecutor, ICapabilities desiredCapabilities) : base(commandExecutor, desiredCapabilities)
        {
   
        }

        public RemoteWebDriverExtended(ICapabilities desiredCapabilities) : base(desiredCapabilities)
        {
        }


        public RemoteWebDriverExtended(Uri remoteAddress, ICapabilities desiredCapabilities) : base(remoteAddress, desiredCapabilities) 
        {
        }


        public RemoteWebDriverExtended(Uri remoteAddress, ICapabilities desiredCapabilities, TimeSpan commandTimeout) : base(remoteAddress, desiredCapabilities, commandTimeout)
        {
        }

        #region Context
        public string Context
        {
            get
            {
                var commandResponse = this.Execute("getContext", null);
                return commandResponse.Value as string;
            }
            set
            {
                var parameters = new Dictionary<string, object>();
                parameters.Add("name", value);
                this.Execute("setContext", parameters);
            }
        }

        public ReadOnlyCollection<string> Contexts
        {
            get
            {
                var commandResponse = this.Execute("contexts", null);
                var contexts = new List<string>();
                var objects = commandResponse.Value as object[];

                if (null != objects && 0 < objects.Length)
                {
                    contexts.AddRange(objects.Select(o => o.ToString()));
                }

                return contexts.AsReadOnly();
            }
        }
        #endregion Context


    }
}

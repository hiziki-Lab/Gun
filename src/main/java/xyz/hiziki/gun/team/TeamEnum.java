package xyz.hiziki.gun.team;

import org.bukkit.ChatColor;

public enum TeamEnum
{
    Red("team_red","赤", ChatColor.RED),
    Blue("team_blue","青",ChatColor.BLUE);

    private final String teamName;
    private final String viewTeamName;
    private final ChatColor teamColor;

    TeamEnum(String _teamName,String _viewTeamName, ChatColor _teamColor)
    {
        teamName = _teamName;
        viewTeamName = _viewTeamName;
        teamColor = _teamColor;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public String getViewTeamName()
    {
        return viewTeamName + "チーム";
    }

    public ChatColor getTeamColor()
    {
        return teamColor;
    }

    public String getPrefix()
    {
        return "【"+this.getViewTeamName()+"】";
    }

    public static TeamEnum getEnum(String teamName)
    {
        for (TeamEnum team : TeamEnum.values() )
        {
            if (team.getTeamName().equals(teamName))
            {
                return team;
            }
        }
        return null;
    }
}